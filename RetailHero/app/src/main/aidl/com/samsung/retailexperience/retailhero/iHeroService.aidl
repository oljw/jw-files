package com.samsung.retailexperience.retailhero;

import com.samsung.retailexperience.retailhero.iHeroServiceCallback;

interface iHeroService {
    void registerCallback(iHeroServiceCallback cb);
    void unregisterCallback(iHeroServiceCallback cb);

    void requestServiceFunc();
}