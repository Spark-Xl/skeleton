package api;

import com.google.cloud.vision.v1.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * Represents the result of an OCR parse
 */
public class ReceiptSuggestionResponse {
    @JsonProperty
    public final String merchantName;

    @JsonProperty
    public final BigDecimal amount;

    @JsonProperty
    public String thumbnail;

    public ReceiptSuggestionResponse(String merchantName, BigDecimal amount) {
        this.merchantName = merchantName;
        this.amount = amount;
    }

    public void setThumbnail(String imageName) {
        this.thumbnail = imageName;
    }
}
