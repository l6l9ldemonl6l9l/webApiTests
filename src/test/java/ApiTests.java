import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class ApiTests {
    @Test
    public void test1_upload() throws IOException {
        RestAssured.baseURI = "https://content.dropboxapi.com/2/files/upload";
        File file = new File("src/img/img.jpg");
        JSONObject uploadObj = new JSONObject();
        uploadObj.put("path", "/Apps/Test_WebAPI/img.jpg");
        uploadObj.put("autorename", true);
        uploadObj.put("mode", "add");
        uploadObj.put("mute", false);

        RequestSpecification uploadRequest = RestAssured.given();
        uploadRequest.headers("Dropbox-API-Arg",uploadObj.toJSONString(),
                "Content-Type","text/plain; charset=dropbox-cors-hack",
                "Authorization", "Bearer " + Creds.token);

        uploadRequest.body(FileUtils.readFileToByteArray(file));
        Response uploadResponse = uploadRequest.post();

        Assert.assertEquals(uploadResponse.getStatusCode(), 200);

    }

    @Test
    public void test2_getMetaData(){
        RestAssured.baseURI = "https://api.dropboxapi.com/2/files/get_metadata";
        RequestSpecification getMetaRequest = RestAssured.given();

        getMetaRequest.headers("Content-Type","application/json",
                "Authorization", "Bearer " + Creds.token);

        JSONObject getMetadataObj = new JSONObject();
        getMetadataObj.put("path", "/Apps/Test_WebAPI/img.jpg");
        getMetadataObj.put("include_media_info", true);
        getMetaRequest.body(getMetadataObj.toJSONString());

        Response getMetaResponse = getMetaRequest.post();
        Assert.assertEquals(getMetaResponse.getStatusCode(), 200);
    }

    @Test
    public void test3_delete(){
        RestAssured.baseURI = "https://api.dropboxapi.com/2/files/delete_v2";
        RequestSpecification deleteRequest = RestAssured.given();

        deleteRequest.headers("Content-Type","application/json",
                "Authorization", "Bearer " + Creds.token);

        JSONObject deleteObj = new JSONObject();
        deleteObj.put("path", "/Apps/Test_WebAPI/img.jpg");
        deleteRequest.body(deleteObj.toJSONString());

        Response deleteResponse = deleteRequest.post();
        Assert.assertEquals(deleteResponse.getStatusCode(), 200);
    }
}
