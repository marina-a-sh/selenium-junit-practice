package org.groundwork.support;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RestAssuredRequestFilter implements Filter, Loggable {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext filterContext) {
        Response response = filterContext.next(requestSpec, responseSpec);
        getLogger().info("\n" +
                         "Request method: " + requestSpec.getMethod() + "\n" +
                         "Request URI: " + requestSpec.getURI() + "\n" +
                         "Request Headers: " + requestSpec.getHeaders() + "\n" +
                         "Request Body: " + requestSpec.getBody() + "\n" +
                         "Response Status: " + response.getStatusCode() + "\n" +
                         "Response Status Line: " + response.getStatusLine() + "\n" +
                         "Response Body: " + response.getBody().asPrettyString() + "\n");
        return response;
    }

}
