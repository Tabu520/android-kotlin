package com.jonbott.knownspies.ModelLayer;

import com.jonbott.knownspies.Helpers.Threading;
import com.jonbott.knownspies.ModelLayer.DTOs.SpyDTO;
import com.jonbott.knownspies.ModelLayer.Database.DataLayer;
import com.jonbott.knownspies.ModelLayer.Database.Realm.Spy;
import com.jonbott.knownspies.ModelLayer.Enums.Source;
import com.jonbott.knownspies.ModelLayer.Network.NetworkLayer;
import com.jonbott.knownspies.ModelLayer.Translation.SpyTranslator;
import com.jonbott.knownspies.ModelLayer.Translation.TranslationLayer;

import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ModelLayerImpl implements ModelLayer {

    private NetworkLayer networkLayer;
    private DataLayer dataLayer;
    private TranslationLayer translationLayer;

    public ModelLayerImpl(NetworkLayer networkLayer, DataLayer dataLayer, TranslationLayer translationLayer) {
        this.networkLayer = networkLayer;
        this.dataLayer = dataLayer;
        this.translationLayer = translationLayer;
    }

    @Override
    public void loadData(Consumer<List<SpyDTO>> onNewResults, Consumer<Source> notifyDataReceived) {
        SpyTranslator spyTranslator = translationLayer.translatorFor(SpyDTO.dtoType);
        try {
            dataLayer.loadSpiesFromLocal(spyTranslator::translate, onNewResults);
            notifyDataReceived.accept(Source.local);
        } catch (Exception e) {
            e.printStackTrace();
        }
        networkLayer.loadJson(json -> {
            notifyDataReceived.accept(Source.network);
//            InputStream inputStream = context.getResources().openRawResource(R.raw.spies);
//            StringBuilder sb = new StringBuilder();
//            BufferedReader br = null;
//            try {
//                br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.spies)));
//                String temp;
//                while ((temp = br.readLine()) != null)
//                    sb.append(temp);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (br != null) {
//                        br.close(); // stop reading
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            persisJson(json, () -> dataLayer.loadSpiesFromLocal(spyTranslator::translate, onNewResults));
        });
    }

    private void persisJson(String json, Action finished) {
        List<SpyDTO> dtos = translationLayer.convertJson(json);
        Threading.async(() -> {
            dataLayer.clearSpies(() -> {
                dtos.forEach(SpyDTO::initialize);
                SpyTranslator translator = translationLayer.translatorFor(SpyDTO.dtoType);
                dataLayer.persistDTOs(dtos, translator::translate);

                Threading.dispatchMain(finished);
            });
            return true;
        });
    }

    @Override
    public SpyDTO spyForId(int spyId) {

        Spy spy = dataLayer.spyForId(spyId);
        SpyDTO spyDTO = translationLayer.translate(spy);
        return spyDTO;
    }

}
