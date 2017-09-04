package controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/netid")
public class NewIDController {
    @GET
    public String getNetID() {
        return "xs293";
    }
}
