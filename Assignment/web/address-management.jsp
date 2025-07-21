<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Address Management</title>
    <link rel="stylesheet" href="assets/css/address-management.css"/>
</head>
<body>

<div class="main-container">
    <h1>Address Management</h1>
    <hr>

    <div class="top-actions">
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="searchAddress" />
            <input type="text" name="strKeyword" placeholder="Search by city or region..." value="${param.strKeyword}" />
            <button type="submit">Search</button>
        </form>

        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toAddAddress" />
            <button type="submit">Add New Address</button>
        </form>
    </div>

    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <table>
        <thead>
            <tr>
                <th>Address</th>
                <th>City</th>
                <th>Region</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="addr" items="${addressList}">
                <tr <c:if test="${addr.id == defaultAddressId}">class="default-address"</c:if>>
                    <td>${addr.fullAddress} ${addr.id == defaultAddressId? '(default)': ''}</td>
                    <td>${addr.city}</td>
                    <td>${addr.region}</td>
                    <td>
                        <form action="MainController" method="post">
                            <input type="hidden" name="action" value="toEditAddress"/>
                            <input type="hidden" name="addressId" value="${addr.id}"/>
                            <input type="hidden" name="countryId" value="${addr.countryId}"/>
                            <input type="hidden" name="unitNumber" value="${addr.unitNumber}"/>
                            <input type="hidden" name="streetNumber" value="${addr.streetNumber}"/>
                            <input type="hidden" name="addressLine1" value="${addr.addressLine1}"/>
                            <input type="hidden" name="addressLine2" value="${addr.addressLine2}"/>
                            <input type="hidden" name="city" value="${addr.city}"/>
                            <input type="hidden" name="region" value="${addr.region}"/>
                            <input type="hidden" name="fullAddress" value="${addr.fullAddress}"/>

                            <button type="submit">Edit</button>
                        </form>
                        <c:if test="${addr.id != defaultAddressId}">
                            <form action="MainController" method="post">
                                <input type="hidden" name="action" value="updateDefaultAddress"/>
                                <input type="hidden" name="addressId" value="${addr.id}"/>
                                <button type="submit">Set as Default</button>
                            </form>

                            <form action="MainController" method="post">
                                <input type="hidden" name="action" value="removeAddress"/>
                                <input type="hidden" name="addressId" value="${addr.id}"/>
                                <button type="submit" onclick="return confirm('Are you sure you want to delete this address?')">Delete</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    
    </table>

    <hr>
    <div class="back-button-container">
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="toWelcome"/>
            <button type="submit">Back</button>
        </form>
    </div>

</div>

</body>
</html>
