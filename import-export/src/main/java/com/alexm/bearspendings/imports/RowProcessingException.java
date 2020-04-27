package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.imports.ExcelRowProcessor.CELL_INDEX;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author AlexM
 * Date: 4/23/20
 **/
public class RowProcessingException extends Exception {
    private transient Row row;
    private CELL_INDEX cellIndex;
    private ERROR_CODE errorCode;

    public enum ERROR_CODE {
        EMPTY_CELL,
        NULL_CELL,
        INVALID_DOUBLE_VALUE,
        INVALID_DATE_VALUE
    }

    public RowProcessingException(ERROR_CODE errorCode, Row row, CELL_INDEX cellIndex, Throwable cause ) {
        super(cause);
        this.errorCode = errorCode;
        this.row = row;
        this.cellIndex = cellIndex;
    }

    @Override
    public String getMessage() {
        switch (this.errorCode) {
            case INVALID_DOUBLE_VALUE:
                return "Exception occurred during extracting double value from row:" + row + " cell index:" + cellIndex;
            case INVALID_DATE_VALUE:
                return "Exception occurred during parsing date in cell:" + cellIndex.index + " from row:" + row;
        }
        return "Generic message";
    }

    public RowProcessingException(ERROR_CODE errorCode, Row row, CELL_INDEX cellIndex ) {
        this(errorCode, row, cellIndex, null);
    }

    public RowProcessingException(Throwable cause) {
        super(cause);
    }

    public RowProcessingException(String message) {
        super(message);
    }

    public RowProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
