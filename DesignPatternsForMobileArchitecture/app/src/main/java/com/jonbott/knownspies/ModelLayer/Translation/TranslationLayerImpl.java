package com.jonbott.knownspies.ModelLayer.Translation;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jonbott.knownspies.ModelLayer.DTOs.SpyDTO;
import com.jonbott.knownspies.ModelLayer.Database.Realm.Spy;
import com.jonbott.knownspies.ModelLayer.Enums.DTOType;

import java.util.List;

import io.realm.Realm;

public class TranslationLayerImpl implements TranslationLayer {
    private static final String TAG = "TranslationLayer";

    private SpyTranslator translator;
    private Gson gson;

    public TranslationLayerImpl(Gson gson, SpyTranslator spyTranslator) {
         this.gson = gson;
         this.translator = spyTranslator;
    }

    @Override
    public List<SpyDTO> convertJson(String json) {
        Log.d(TAG, "Converting json to dtos");

        TypeToken<List<Spy>> token = new TypeToken<List<Spy>>() {};
        return gson.fromJson(json, token.getType());
    }

    @Override
    public  SpyTranslator translatorFor(DTOType type) {
        switch (type) {
            case spy:
                return translator;
            default:
                return translator;
        }
    }

    @Override
    public SpyDTO translate(Spy spy) {
        SpyDTO dto = translator.translate(spy);
        return dto;
    }

    @Override
    public Spy translate(SpyDTO dto, Realm realm) {
        Spy spy = translator.translate(dto, realm);
        return spy;
    }
}
