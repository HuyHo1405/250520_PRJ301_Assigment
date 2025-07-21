<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Profile</title>
        <!-- Link to Google Fonts for Inter -->
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="assets/css/profile.css"/>
        
    </head>
    <body>
        <!-- Main Profile Layout Container -->
        <div class="main-profile-layout">
            <!-- Left Column for Account and Address Info -->
            <div class="left-column">
                <h1>${sessionScope.user.email_address} Profile</h1>
                
                <hr>

                <!-- Section: Account Info -->
                <div class="profile-section">
                    <h2 class="section-header">Account Information</h2>
                    <form action="MainController" method="post">
                        <label for="email">Email:</label>
                        <input type="email" id="email" name="email" value="${sessionScope.user.email_address}" required><br>
                        
                        <label for="phone">Phone:</label>
                        <input type="text" id="phone" name="phone" value="${sessionScope.user.phone_number}" required><br>
                        
                        <button name="action" value="Update Profile">Update</button>
                    </form>
                </div>

                <hr>

                <!-- Section: Address Info -->
                <div class="profile-section">
                    <h2 class="section-header">Address Information</h2>
                    <c:choose>
                        <c:when test="${empty addressList}">
                            <p>You have no saved addresses.</p>
                            <form action="MainController" method="post">
                                <button name="action" value="toAddressManagement">Manage Addresses</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form action="MainController" method="post">
                                <label for="addressId">Select Default Address:</label>
                                <select id="addressId" name="addressId" required>
                                    <c:forEach var="addr" items="${addressList}">
                                        <option value="${addr.id}" <c:if test="${addr.id == defaultAddressId}">selected</c:if>>
                                            ${addr.fullAddress}
                                        </option>
                                    </c:forEach>
                                </select><br>
                                <button name="action" value="updateDefaultAddress">Update Default Address</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <!-- Back to Home button (inside left column, aligned left, normal size) -->
                <hr> <!-- Giữ lại đường kẻ ngang trước nút Back to Home -->
                <form action="MainController" method="post" class="back-to-home-form">
                    <button name="action" value="toWelcome">Back to Home</button>
                </form>
            </div>

            <!-- Right Column for Admin Action -->
                <div class="right-column">
            <c:if test="${sessionScope.user.role eq 'admin'}">
                    <h2>Admin Action</h2>
                    <form action="MainController" method="post">
                        <button name="action" value="toSystemConfigManagement">System Configuration Management</button>
                    </form>
            </c:if>
                </div>
        </div>
    </body>
</html>
