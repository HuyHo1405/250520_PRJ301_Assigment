<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Management</title>
    <link rel="stylesheet" href="assets/css/user-management.css">
</head>
<body>
<div class="layout-container">
    <!-- Sidebar -->
    <jsp:include page="assets/components/sidebar.jsp" />

    <!-- Main Content -->
    <div class="content-container">
        <h1>User Management</h1>
        <hr>

        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Email Address</th>
                    <th>Phone Number</th>
                    <th>Password</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="editId" value="${param.editId}" />
                <c:set var="addMode" value="${param.addMode eq 'true'}" />

                <c:choose>
                    <c:when test="${not empty requestScope.userList}">
                        <c:forEach var="user" items="${requestScope.userList}">
                            <c:set var="isEditing" value="${user.id == editId}" />
                            <c:choose>
                                <c:when test="${isEditing}">
                                    <tr class="editing-row">
                                        <td>${user.id}</td>
                                        <td>${user.email_address}</td>
                                        <td>${user.phone_number}</td>
                                        <td class="password-cell">${user.hashed_password}</td>
                                        <td>
                                            <input form="editUser" type="hidden" name="action" value="updateUser"/>
                                            <input form="editUser" type="hidden" name="userId" value="${user.id}"/>
                                            <select form="editUser" name="newRole" class="select-field">
                                                <option value="user" ${user.role eq 'user' ? 'selected' : ''}>user</option>
                                                <option value="admin" ${user.role eq 'admin' ? 'selected' : ''}>admin</option>
                                            </select>
                                        </td>
                                        <td>
                                            <select form="editUser" name="isActive" class="select-field">
                                                <option value="1" ${user.is_active ? 'selected' : ''}>Active</option>
                                                <option value="0" ${!user.is_active ? 'selected' : ''}>Not Active</option>
                                            </select>
                                        </td>
                                        <td class="action-buttons">
                                            <button form="editUser" type="submit" class="button-primary">Update</button>
                                            <button form="cancel" type="submit" name="action" value="toAdminUserPage" class="button-secondary">Cancel</button>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr class="display-row">
                                        <td>${user.id}</td>
                                        <td><c:out value="${user.email_address}"/></td>
                                        <td><c:out value="${user.phone_number}"/></td>
                                        <td class="password-cell"><c:out value="${user.hashed_password}"/></td>
                                        <td><c:out value="${user.role}"/></td>
                                        <td>
                                            <span class="${user.is_active ? 'status-active' : 'status-inactive'}">
                                                ${user.is_active ? 'Active' : 'Not Active'}
                                            </span>
                                        </td>
                                        <td class="action-buttons">
                                            <c:if test="${user.id != sessionScope.user.id}">
                                                <form action="MainController" method="post" style="display: inline;">
                                                    <input type="hidden" name="action" value="toAdminUserPage"/>
                                                    <button type="submit" name="editId" value="${user.id}" class="button-edit">Edit</button>
                                                </form>

                                                <form action="MainController" method="post" style="display: inline;">
                                                    <input type="hidden" name="action" value="changeUserRole"/>
                                                    <input type="hidden" name="userId" value="${user.id}"/>
                                                    <input type="hidden" name="newRole" value="${user.role eq 'admin' ? 'user' : 'admin'}"/>
                                                    <button type="submit" class="button-change-role">Change Role</button>
                                                </form>

                                                <form action="MainController" method="post" style="display: inline;">
                                                    <input type="hidden" name="userId" value="${user.id}"/>
                                                    <input type="hidden" name="action" value="toggleIsActiveUser"/>
                                                    <button type="submit"
                                                            onclick="return confirm('${user.is_active? 'Do you want to deactivate this user?' : 'Do you want to activate this user?'}')"
                                                            class="${user.is_active ? 'button-deactivate' : 'button-activate'}">
                                                        ${user.is_active ? 'Deactivate' : 'Activate'}
                                                    </button>
                                                </form>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="7" class="no-users-found">There is no user.</td></tr>
                    </c:otherwise>
                </c:choose>

                <c:if test="${addMode || empty requestScope.userList}">
                    <tr class="add-user-row">
                        <td>(New)</td>
                        <td><input form="addUser" type="email" name="email" value="${param.email}" required class="input-field" placeholder="Email"/></td>
                        <td><input form="addUser" type="text" name="phone" value="${param.phone}" required class="input-field" placeholder="Phone Number"/></td>
                        <td><input form="addUser" type="password" name="password" required class="input-field" placeholder="Password"/></td>
                        <td>
                            <select form="addUser" name="role" class="select-field">
                                <option value="user" ${param.role eq 'user' ? 'selected' : ''}>user</option>
                                <option value="admin" ${param.role eq 'admin' ? 'selected' : ''}>admin</option>
                            </select>
                        </td>
                        <td>
                            <select form="addUser" name="isActive" class="select-field">
                                <option value="true" ${param.isActive eq 'true' ? 'selected' : ''}>Active</option>
                                <option value="false" ${param.isActive eq 'false' ? 'selected' : ''}>Not Active</option>
                            </select>
                        </td>
                        <td class="action-buttons">
                            <input form="addUser" type="hidden" name="action" value="createUser"/>
                            <button form="addUser" type="submit" class="button-primary">Create</button>
                            <button form="cancel" type="submit" name="action" value="toAdminUserPage" class="button-secondary">Cancel</button>
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <div class="add-user-button-container">
            <form action="MainController" method="post">
                <input type="hidden" name="addMode" value="true"/>
                <button name="action" value="toAdminUserPage" class="button-add-new">Add New User</button>
            </form>
        </div>

        <form id="editUser" action="MainController" method="post"></form>
        <form id="addUser" action="MainController" method="post"></form>
        <form id="cancel" action="MainController" method="post"></form>

        <hr>

        <div class="back-button-container">
            <form action="MainController" method="post">
                <button name="action" value="toWelcome" class="button-secondary">Back</button>
            </form>
        </div>

        <c:if test="${not empty requestScope.error}">
            <p class="error-message">${requestScope.error}</p>
        </c:if>
    </div>
</div>
</body>
</html>
