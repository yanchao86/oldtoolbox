/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:FetchWeatherDataWorker.java Project: LvWeatherService
 * 
 * Creator:4399-lvtu-8 
 * Date:Dec 2, 2013 6:53:53 PM
 * 
 */
package com.pixshow.framework.dtask.internal.adapter.gearman;

import org.gearman.client.GearmanJobResult;
import org.gearman.client.GearmanJobResultImpl;
import org.gearman.util.ByteUtils;
import org.gearman.worker.AbstractGearmanFunction;

import com.pixshow.framework.dtask.api.DWorker;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 2, 2013
 * 
 */

public class ExecuterGearmanFunction extends AbstractGearmanFunction {

    private DWorker worker;

    public ExecuterGearmanFunction(DWorker worker) {
        this.worker = worker;
    }

    @Override
    public GearmanJobResult executeFunction() {
        String data = ByteUtils.fromUTF8Bytes((byte[]) this.data);
        String result = worker.execute(data);
        return new GearmanJobResultImpl(this.jobHandle, true, ByteUtils.toUTF8Bytes(result), new byte[0], new byte[0], 0, 0);
    }

}
