package com.tecace.retail.appmanager;

import com.tecace.retail.appmanager.iRetailAppServiceCallback;

interface iRetailAppService {
    void registerCallback(iRetailAppServiceCallback cb);
    void unregisterCallback(iRetailAppServiceCallback cb);

    void requestServiceFunc();
}