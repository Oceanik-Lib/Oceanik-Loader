package com.yallage.oceanik.loader;

import lombok.Getter;

/**
 * @author Milkory
 */
@SuppressWarnings("unused")
public class VersionInfo {
    @Getter private int id;
    @Getter private String tag;
    @Getter private String url;
    @Getter private String sha256;
}
