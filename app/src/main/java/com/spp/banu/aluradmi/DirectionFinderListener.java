package com.spp.banu.aluradmi;


import com.spp.banu.aluradmi.model.Gedung;
import com.spp.banu.aluradmi.model.Rute;

import java.util.List;

/**
 * Created by banu on 30/12/16.
 */

public interface DirectionFinderListener {
    void DirectionFinderStart();
    void DirectionFinderFailed();
    void DirectionFinderSuccess(List<Rute> rutes, Gedung destination_gedung);
}
