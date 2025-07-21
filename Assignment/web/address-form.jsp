<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${formTitle != null ? formTitle : 'Address Form'}</title>
    <link rel="stylesheet" href="assets/css/address-form.css"/>
</head>
<body>

<div class="form-container">
    <h1>${formTitle != null ? formTitle : 'Address Form'}</h1>
    <hr>

    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <form action="MainController" method="post">
        <input type="hidden" name="action" value="${action}" />

        <c:if test="${not empty param.addressId}">
            <input type="hidden" name="addressId" value="${param.addressId}" />
        </c:if>

        <label for="countryId">Country:</label>
        <select name="countryId" id="countryId" required>
            <option value="">-- Select country --</option>
            <c:forEach var="country" items="${countries}">
                <option value="${country.id}"
                        <c:if test="${param.countryId == country.id}">selected</c:if>>
                    ${country.country_name}
                </option>
            </c:forEach>
        </select>

        <label for="unitNumber">Unit Number:</label>
        <input type="text" name="unitNumber" id="unitNumber" value="${param.unitNumber}" maxlength="20">
        
        <label for="streetNumber">Street Number:</label>
        <input type="text" name="streetNumber" id="streetNumber" value="${param.streetNumber}" maxlength="20">
        
        <label for="addressLine1">Address Line 1:</label>
        <input type="text" name="addressLine1" id="addressLine1" value="${param.addressLine1}" required maxlength="255">
        
        <label for="addressLine2">Address Line 2:</label>
        <input type="text" name="addressLine2" id="addressLine2" value="${param.addressLine2}" maxlength="255">
        
        <label for="city">City:</label>
        <input type="text" name="city" id="city" value="${param.city}" required maxlength="100">
        
        <label for="region">Region:</label>
        <input type="text" name="region" id="region" value="${param.region}" maxlength="100">

        <button type="submit">
            <c:choose>
                <c:when test="${action eq 'updateAddress'}">Update</c:when>
                <c:otherwise>Add New</c:otherwise>
            </c:choose>
        </button>
    </form>

    <br>
    <div class="back-button-container">
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toAddressManagement" />
            <button type="submit">Back to List</button>
        </form>
    </div>

</div>

</body>
</html>
    