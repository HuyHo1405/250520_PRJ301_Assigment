<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>User Management</h1>
        <hr>

        <table border="1" style="border-collapse:collapse;">
            <tr>
                <th>ID</th>
                <th>Email Address</th>
                <th>Phone Number</th>
                <th>Password</th>
                <th>Role</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
            <c:set var="editId" value="${param.editId}" /> <%-- Get editId from request param --%>
            <c:set var="addMode" value="${param.addMode eq 'true'}" />
            <c:choose>
                <c:when test="${not empty requestScope.userList}">

                    <c:forEach var="user" items="${requestScope.userList}">
                        <c:set var="isEditing" value="${user.id == editId}" />

                        <c:choose>
                            <c:when test="${isEditing}">
                                <tr>
                                    <td>${user.id}</td>
                                    <td>${user.email_address}</td>
                                    <td>${user.phone_number}</td>
                                    <td>${user.hashed_password}</td>
                                    <td>
                                        <input form="editUser" type="hidden" name="action" value="updateUser"/>
                                        <input form="editUser" type="hidden" name="userId" value="${user.id}"/>
                                        <select form="editUser" name="newRole">
                                            <option value="user" ${user.role eq 'user' ? 'selected' : ''}>user</option>
                                            <option value="admin" ${user.role eq 'admin' ? 'selected' : ''}>admin</option>
                                        </select>
                                    </td>
                                    <td>
                                        <select form="editUser" name="isActive">
                                            <option value="1" ${user.is_active ? 'selected' : ''}>Active</option>
                                            <option value="0" ${!user.is_active ? 'selected' : ''}>Not Active</option>
                                        </select>
                                    </td>
                                    <td>
                                        <button form="editUser" type="submit">Update</button>
                                        <button form="cancel" type="submit" name="action" value="toAdminUserPage">Cancel</button>
                                    </td>
                                </tr>    
                            </c:when>
                            <c:otherwise>
                                <%-- Display mode --%>
                                <tr>
                                    <td>${user.id}</td>
                                    <td><c:out value="${user.email_address}"/></td>
                                    <td><c:out value="${user.phone_number}"/></td>
                                    <td style="max-width: 200px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;"><c:out value="${user.hashed_password}"/></td>
                                    <td><c:out value="${user.role}"/></td>
                                    <td>
                                        <span class="${user.is_active ? 'status-active' : 'status-inactive'}">
                                            ${user.is_active ? 'Active' : 'Not Active'}
                                        </span>
                                    </td>
                                    <td>
                                        <c:if test="${user.id != sessionScope.user.id}">
                                            <form action="MainController" method="post" style="display: inline;">
                                                <input type="hidden" name="action" value="toAdminUserPage"/>
                                                <button type="submit" name="editId" value="${user.id}">Edit</button>
                                            </form>

                                            <form action="MainController" method="post" style="display: inline;">
                                                <input type="hidden" name="action" value="changeUserRole"/>
                                                <input type="hidden" name="userId" value="${user.id}"/>
                                                <input type="hidden" name="newRole" value="${user.role eq 'admin' ? 'user' : 'admin'}"/>
                                                <button type="submit">Change Role</button>
                                            </form>

                                            <form action="MainController" method="post" style="display: inline;">
                                                <input type="hidden" name="userId" value="${user.id}"/>
                                                <input type="hidden" name="action" value="toggleIsActiveUser"/>
                                                <button type="submit" onclick="return confirm(${user.is_active? 'Do you want to deactivate this user?': 'Do you want to activate this user?'})">${user.is_active? 'Deactivate': 'Activate'}</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr><td colspan="6">There is no user.</td></tr>
                </c:otherwise>
            </c:choose>
            <c:if test="${addMode || empty requestScope.userList}">
                <tr>
                    <td>(New)</td>
                    <td><input form="addUser" type="email" name="email" value="${param.email}" required/></td>
                    <td><input form="addUser" type="text" name="phone" value="${param.phone}" required/></td>
                    <td><input form="addUser" type="password" name="password" value="" required/></td>
                    <td>
                        <select form="addUser" name="role">
                            <option value="user" ${param.role eq 'user' ? 'selected' : ''}>user</option>
                            <option value="admin" ${param.role eq 'admin' ? 'selected' : ''}>admin</option>
                        </select>
                    </td>
                    <td>
                        <select form="addUser" name="isActive">
                            <option value="${true}" ${param.isActive eq true ? 'selected' : ''}>Active</option>
                            <option value="${false}" ${param.isActive eq false? 'selected' : ''}>Not Active</option>
                        </select>
                    </td>
                    <td>
                        <input form="addUser" type="hidden" name="action" value="createUser"/>
                        <button form="addUser" type="submit">Create</button>
                        <button form="cancel" type="submit" name="action" value="toAdminUserPage">Cancel</button>
                    </td>
                </tr>
            </c:if>
        </table>
            <form action="MainController">
                <input type="hidden" name="addMode" value="${true}"/>
                <button name="action" value="toAdminUserPage">Add New User</button>
            </form>

        <form id="editUser" action="MainController" method="post"></form>
        <form id="addUser" action="MainController" method="post"></form>
        <form id="cancel" action="MainController" method="post"></form>
    </body>

    <hr>

    <form action="MainController" method="post">
        <button name="action" value="toSystemConfigManagement">Back</button>
    </form>
    <c:if test="${not empty requestScope.error}">
        <p style="color: red;">${requestScope.error}</p>
    </c:if>
</html>
