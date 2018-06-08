package controller;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by Aashish Sharma on 8/6/18.
 */

public class CheckValidation {

    /**
     * @param cityName
     * @return true if the cityName is not null and not empty
     */
    public boolean isCityNameValid(@NonNull String cityName) {
        return !TextUtils.isEmpty(cityName);
    }

    /**
     * @param cityPopulation
     * @return true if the cityPopulation is not null and not empty
     */
    public boolean isCityPopulationValid(@NonNull String cityPopulation) {
        return !TextUtils.isEmpty(cityPopulation);
    }

    /**
     * @param cityState
     * @return true if the cityPopulation is not null and not empty
     */
    public boolean isCityStateValid(@NonNull String cityState) {
        return !TextUtils.isEmpty(cityState);
    }




}
