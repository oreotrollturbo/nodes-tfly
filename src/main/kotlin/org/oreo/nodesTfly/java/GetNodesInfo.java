package org.oreo.nodesTfly.java;

import phonon.nodes.war.FlagWar;

public class GetNodesInfo {

    /**
     *  yes I know this is terrible code, but I couldn't find how to do this in kotlin for th life of me
     * @return boolean war is on
     */
    public static boolean isWarOn(){
        return FlagWar.INSTANCE.getEnabled$nodes();
    }
}
