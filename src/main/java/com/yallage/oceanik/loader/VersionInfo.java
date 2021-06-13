package com.yallage.oceanik.loader;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Milkory
 */
@Getter
@ToString
public class VersionInfo {
    int id;
    String tag;
    String url;
    String sha256;
}
