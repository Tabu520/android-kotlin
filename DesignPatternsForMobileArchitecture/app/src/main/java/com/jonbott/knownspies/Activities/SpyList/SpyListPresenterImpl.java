package com.jonbott.knownspies.Activities.SpyList;

import com.jonbott.knownspies.ModelLayer.DTOs.SpyDTO;
import com.jonbott.knownspies.ModelLayer.Enums.Gender;
import com.jonbott.knownspies.ModelLayer.Enums.Source;
import com.jonbott.knownspies.ModelLayer.ModelLayer;

import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import io.realm.Realm;

public class SpyListPresenterImpl implements SpyListPresenter {

    private static final String TAG = "SpyListPresenter";

    private ModelLayer modelLayer;
    private BehaviorSubject<List<SpyDTO>> spies = BehaviorSubject.create();

    public SpyListPresenterImpl(ModelLayer modelLayer) {
        this.modelLayer = modelLayer;
    }

    //region Presenter Methods

    @Override
    public void loadData(Consumer<Source> notifyDataReceived) {
        modelLayer.loadData(this::onDataLoaded, notifyDataReceived);
    }

    private void onDataLoaded(List<SpyDTO> spyDTOList) {
        spies.onNext(spyDTOList);
    }

    @Override
    public void addNewSpy() {
        String name = "Pham The Tai";
        List<SpyDTO> newSpies = Arrays.asList(new SpyDTO(100, 25, name, Gender.male, "wealth", "thetai", true));
        modelLayer.save(newSpies, () -> {
            SpyDTO theTai = modelLayer.spyForName(name);
            List<SpyDTO> spyList = spies.getValue();
            spyList.add(0, theTai);

            spies.onNext(spyList);
        });
    }

    @Override
    public BehaviorSubject<List<SpyDTO>> spies() {
        return spies;
    }

    //endregion

}
