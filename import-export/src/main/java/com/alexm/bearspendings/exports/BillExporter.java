package com.alexm.bearspendings.exports;

import java.io.InputStream;

/**
 * @author AlexM
 * Date: 12/10/20
 **/
public interface BillExporter {
    InputStream exportAll();
}
