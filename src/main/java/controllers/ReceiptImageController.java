package controllers;

import api.ReceiptSuggestionResponse;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Collections;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.hibernate.validator.constraints.NotEmpty;
import com.google.cloud.vision.v1.Vertex;
import com.google.cloud.vision.v1.BoundingPoly;
import java.util.List;

import static java.lang.System.out;

@Path("/images")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptImageController {
    private final AnnotateImageRequest.Builder requestBuilder;

    public ReceiptImageController() {
        // DOCUMENT_TEXT_DETECTION is not the best or only OCR method available
        Feature ocrFeature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        this.requestBuilder = AnnotateImageRequest.newBuilder().addFeatures(ocrFeature);

    }

    /**
     * This borrows heavily from the Google Vision API Docs.  See:
     * https://cloud.google.com/vision/docs/detecting-fulltext
     *
     * YOU SHOULD MODIFY THIS METHOD TO RETURN A ReceiptSuggestionResponse:
     *
     * public class ReceiptSuggestionResponse {
     *     String merchantName;
     *     String amount;
     * }
     */
    @POST
    public ReceiptSuggestionResponse parseReceipt(@NotEmpty String base64EncodedImage) throws Exception {
        Image img = Image.newBuilder().setContent(ByteString.copyFrom(Base64.getDecoder().decode(base64EncodedImage))).build();
        AnnotateImageRequest request = this.requestBuilder.setImage(img).build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse responses = client.batchAnnotateImages(Collections.singletonList(request));
            AnnotateImageResponse res = responses.getResponses(0);

            String merchantName = null;
            BigDecimal amount = null;

            Integer left_x = null;
            Integer right_x = null;
            Integer top_y = null;
            Integer bottom_y = null;

            // Your Algo Here!!
            // Sort text annotations by bounding polygon.  Top-most non-decimal text is the merchant
            // bottom-most decimal text is the total amount
            for (int i = 0; i < res.getTextAnnotationsList().size(); i ++) {
                EntityAnnotation annotation = res.getTextAnnotationsList().get(i);
                String text = annotation.getDescription();
                // save the full text bounding

                BoundingPoly bp = annotation.getBoundingPoly();
                if (i == 0) {
                    for (int j = 0; j < 4; j++) {
                        Vertex v= bp.getVertices(j);
                        Integer x = v.getX();
                        Integer y = v.getY();

                        if (left_x == null) {
                            left_x = x;
                            right_x = x;
                        } else {
                            left_x = new Integer(Math.min(x, left_x));
                            right_x = new Integer(Math.max(x, right_x));
                        }

                        if (top_y == null) {
                            top_y = y;
                            bottom_y = y;
                        } else {
                            top_y = new Integer(Math.min(top_y, y));
                            bottom_y = new Integer(Math.max(bottom_y, y));
                        }
                    }
                    continue;
                }

                if (getDecimal(text) == null) {
                    merchantName = text;
                    break;
                }
            }

            for (int i = res.getTextAnnotationsList().size() - 1; i >= 0; i --) {
                String text = res.getTextAnnotationsList().get(i).getDescription();
                amount = getDecimal(text);
                if (amount != null)
                    break;
            }

            //TextAnnotation fullTextAnnotation = res.getFullTextAnnotation();
            ReceiptSuggestionResponse response = new ReceiptSuggestionResponse(merchantName, amount);
            response.setBoundrary(left_x, right_x, top_y, bottom_y);

            return response;
        }
    }

    public BigDecimal getDecimal(@NotEmpty String s) {
        try {
            BigDecimal bd = new BigDecimal(s);
            return bd;
        }
        catch(NumberFormatException e) {
            return null;
        }
        // long value = bd.longValue();
    }
}
