<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${formTitle != null ? formTitle : 'Address Form'}</title>
</head>
<body>

<c:set var="error" value="${empty error ? '' : error}" />
<c:set var="action" value="${actionType eq 'editAddress' ? 'updateAddress' : 'addAddress'}" />
<c:set var="countries" value="${not empty countries ? countries : emptyList}" />
<c:if test="${countries == null}"><c:set var="countries" value="${emptyList}" /></c:if>

<h1>${formTitle != null ? formTitle : 'Address Form'}</h1>
<hr>

<c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
</c:if>

<form action="MainController" method="post">
    <input type="hidden" name="action" value="${action}" />

    <c:if test="${not empty param.addressId}">
        <input type="hidden" name="addressId" value="${param.addressId}" />
    </c:if>

    Country:
    <select name="countryId" required>
        <option value="">-- Select country --</option>
        <c:forEach var="country" items="${countries}">
            <option value="${country.id}"
                    <c:if test="${param.countryId == country.id}">selected</c:if>>
                ${country.country_name}
            </option>
        </c:forEach>
    </select><br>

    Unit Number: <input type="text" name="unitNumber" value="${param.unitNumber}" maxlength="20"><br>
    Street Number: <input type="text" name="streetNumber" value="${param.streetNumber}" maxlength="20"><br>
    Address Line 1: <input type="text" name="addressLine1" value="${param.addressLine1}" required maxlength="255"><br>
    Address Line 2: <input type="text" name="addressLine2" value="${param.addressLine2}" maxlength="255"><br>
    City: <input type="text" name="city" value="${param.city}" required maxlength="100"><br>
    Region: <input type="text" name="region" value="${param.region}" maxlength="100"><br>

    <button type="submit">
        <c:choose>
            <c:when test="${action eq 'updateAddress'}">Update</c:when>
            <c:otherwise>Add New</c:otherwise>
        </c:choose>
    </button>
</form>

<br>
<form action="MainController" method="post">
    <input type="hidden" name="action" value="toAddressManagement" />
    <button type="submit">Back to List</button>
</form>

</body>
</html>
    