package com.dronecontrol.droneapi.data;

import com.google.common.collect.ImmutableList;

public class VisionData {
    public static final VisionData NO_DATA = new VisionData(ImmutableList.<VisionTagData>of());

    private final ImmutableList<VisionTagData> tagData;

    public VisionData(ImmutableList<VisionTagData> tagData) {
        this.tagData = tagData;
    }

    public ImmutableList<VisionTagData> getTags() {
        return tagData;
    }
}