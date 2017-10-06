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
    public Integer left_x;

	@JsonProperty
    public Integer right_x;

    @JsonProperty
    public Integer top_y;

    @JsonProperty
    public Integer bottom_y;

    public ReceiptSuggestionResponse(String merchantName, BigDecimal amount) {
        this.merchantName = merchantName;
        this.amount = amount;

        this.left_x = null;
		this.right_x = null;
		this.top_y = null;
		this.bottom_y = null;
    }

    public void setBoundrary(Integer left_x, Integer right_x, Integer top_y, Integer bottom_y) {
        this.left_x = left_x;
		this.right_x = right_x;
		this.top_y = top_y;
		this.bottom_y = bottom_y;
    }
}
