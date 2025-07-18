<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<%
    // Nếu chưa có editId thì gán rỗng cho chắc ăn
    String editId = request.getParameter("editId");
    if (editId == null) editId = "";
    // Retrieve addModeType from request attributes. This attribute is set by MainController
    // when the "Add New" button is clicked for a specific configuration type.
    String addModeType = (String) request.getAttribute("addModeType");
    if (addModeType == null) addModeType = "";
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Config Table</title>
    </head>
    <body>

        <c:set var="type" value="${param.type}" />
        <c:set var="fieldName" value="${param.fieldName}" />
        <c:set var="list" value="${requestScope[param.listName]}" />
        <c:set var="categoryList" value="${requestScope.categoryList}" />

        <table border="1" style="border-collapse:collapse;">
            <tr>
                <th>ID</th>
                <th>Value</th>
                    <c:if test="${type eq 'category'}">
                    <th>Parent</th>
                    </c:if>
                <c:if test="${type eq 'shippingMethod'}">
                    <th>Price</th>
                    </c:if>
                <th>Status</th>
                <th>Action</th>
            </tr>

            <form id="editForm" action="MainController" method="post" style="display: inline"></form>

            <c:forEach var="dto" items="${list}">
                <c:set var="id" value="${dto.id}" />
                <c:choose>
                    <c:when test="${dto.id == editId && type eq editType}">
                        <tr>
                            <td><input form="editForm" type="text" name="id" value="${dto.id}" readonly /></td>
                            <td><input form="editForm" type="text" name="value" value="${dto[fieldName]}" /></td>
                                <c:if test="${type eq 'category'}">
                                <td>
                                    <select form="editForm" name="parentId">
                                        <option value="-1">-- Không có --</option>
                                        <c:forEach var="cat" items="${categoryList}">
                                            <c:if test="${cat.id != dto.id}">
                                                <option value="${cat.id}" ${cat.id == dto.parent_category_id ? 'selected' : ''}>${cat.name}</option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </td>
                            </c:if>

                            <c:if test="${type eq 'shippingMethod'}">
                                <td><input form="editForm" type="number" name="price" value="${dto.price}" /></td>
                            </c:if>
                            <td>
                                <select form="editForm" name="isActive">
                                    <option value="${true}" ${dto.is_active ? 'selected' : ''}>Active</option>
                                    <option value="${false}" ${!dto.is_active ? 'selected' : ''}>Not Active</option>
                                </select>
                            </td>
                            <td>
                                <input form="editForm" type="hidden" name="type" value="${type}" />
                                <input form="editForm" type="hidden" name="action" value="updateSystemConfig" />
                                <button form="editForm" type="submit">Save</button>
                                <form action="MainController" method="post" style="display: inline">
                                    <button name="action" value="toSystemConfigManagement">Cancel</button>
                                </form>
                            </td>
                        </tr>
                    </c:when>

                    <c:otherwise>
                        <tr>
                            <td>${dto.id}</td>
                            <td>${dto[fieldName]}</td>
                            <c:if test="${type eq 'shippingMethod'}">
                                <td>${dto.price}</td>
                            </c:if>
                            <c:if test="${type eq 'category'}">
                                <td>${dto.parent_category_id == -1? '-': dto.parent_category_id} </td>
                            </c:if>
                            <td>${dto.is_active ? 'Active': 'Not Active'}</td>
                            <td>
                                <form action="MainController" method="post" style="display:inline;">
                                    <input type="hidden" name="type" value="${type}" />
                                    <input type="hidden" name="fieldName" value="${fieldName}" />
                                    <input type="hidden" name="editType" value="${type}" />
                                    <input type="hidden" name="action" value="toSystemConfigManagement" />
                                    <button type="submit" name="editId" value="${dto.id}">Edit</button>
                                </form>

                                <form action="MainController" method="post" style="display:inline;">
                                    <input type="hidden" name="type" value="${type}" />
                                    <input type="hidden" name="id" value="${dto.id}" />
                                    <input type="hidden" name="status" value="${dto.is_active}" />
                                    <input type="hidden" name="action" value="toggleIsActiveSystemConfig" />
                                    <button type="submit" onclick="return confirm('${dto.is_active? 'Deactivate': 'Activate'} this item?')">${dto.is_active? 'Deactivate': 'Activate'}</button>
                                </form> 
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${empty editId && (addModeType eq type)}">
                <form id="addForm" action="MainController" method="post" style="display: inline"></form>
                <tr>
                    <td>(New)</td>
                    <td><input form="addForm" type="text" name="value" placeholder="Enter value" required /></td>

                    <c:if test="${type eq 'category'}">
                        <td>
                            <select form="addForm" name="parentId">
                                <option value="-1">-- Không có --</option>
                                <c:forEach var="cat" items="${categoryList}">
                                    <option value="${cat.id}">${cat.name}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </c:if>
                    <c:if test="${type eq 'shippingMethod'}">
                        <td><input form="addForm" type="number" name="price" placeholder="Enter price" required min="0" step="0.01" /></td>
                        </c:if>
                    <td>
                        Active
                    </td>
                    <td>
                        <input form="addForm" type="hidden" name="type" value="${type}" />
                        <input form="addForm" type="hidden" name="action" value="addSystemConfig" /> <%-- New action for adding --%>
                        <button form="addForm" type="submit">Save New</button>
                        <form action="MainController" method="post" style="display: inline">
                            <button name="action" value="toSystemConfigManagement">Cancel</button>
                        </form>
                    </td>
                </tr>

            </c:if>

            <c:if test="${empty list}">
                <tr><td colspan="4">No data available.</td></tr>
            </c:if>
        </table>

        <c:if test="${empty editId && (addModeType eq '' || addModeType ne type)}">
            <form action="MainController" method="post" >
                <input type="hidden" name="action" value="toSystemConfigManagement" />
                <input type="hidden" name="addModeType" value="${type}" /> 
                <button type="submit" >Add New</button>
            </form>
        </c:if>


    </body>
</html>
