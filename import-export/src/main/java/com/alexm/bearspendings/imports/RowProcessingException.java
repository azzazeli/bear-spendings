package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.imports.ExcelRowProcessor.CELL_COLUMN;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author AlexM
 * Date: 4/23/20
 **/
public class RowProcessingException extends Exception {
    private transient Row row;
    private CELL_COLUMN cellIndex;
    private ERROR_CODE errorCode;

    public enum ERROR_CODE {
        EMPTY_CELL,
        NULL_CELL,
        INVALID_DOUBLE_VALUE,
        INVALID_DATE_VALUE
    }

    public RowProcessingException(ERROR_CODE errorCode, Row row, CELL_COLUMN cellIndex, Throwable cause ) {
        super(cause);
        this.errorCode = errorCode;
        this.row = row;
        this.cellIndex = cellIndex;
    }

    @Override
    public String getMessage() {
        switch (this.errorCode) {
            case INVALID_DOUBLE_VALUE:
                return String.format("Exception occurred during extracting double value from cell index:%d row:%s", cellIndex.index, row);
            case INVALID_DATE_VALUE:
                return String.format("Exception occurred during parsing date in cell index:%d from row:%s", cellIndex.index, row);
            case EMPTY_CELL:
                return String.format("No value name found in provided cell. column index:%d from row:%s", cellIndex.index, row);
            case NULL_CELL:
                return String.format("Null cell with index %d found in row: %s", cellIndex.index, row);
            default:
                return "Generic error";
        }
    }

    public RowProcessingException(ERROR_CODE errorCode, Row row, CELL_COLUMN cellIndex ) {
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
