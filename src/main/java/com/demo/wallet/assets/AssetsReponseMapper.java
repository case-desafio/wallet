package com.demo.wallet.assets;

import com.demo.wallet.entity.Assets;

public class AssetsReponseMapper {

    public static AssetsResponse mapper(Assets assets) {
        return new AssetsResponse(
                assets.getTicker(),
                assets.getQuantity(),
                assets.getAveragePrice(),
                assets.getTotalPrice(),
                assets.getUserAccountEmail()
        );
    }
}
