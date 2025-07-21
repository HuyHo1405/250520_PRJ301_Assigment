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
        <link rel="stylesheet" href="assets/css/config-table.css"/>
        <title>Config Table</title>
    </head>
<body class="bg-gray-100 px-6 py-4">
    <c:set var="type" value="${param.type}" />
    <c:set var="fieldName" value="${param.fieldName}" />
    <c:set var="list" value="${requestScope[param.listName]}" />
    <c:set var="categoryList" value="${requestScope.categoryList}" />

    <div class="container mx-auto w-auto bg-white p-6 rounded shadow-md">

        <table class="min-w-full table-auto border border-gray-300 divide-y divide-gray-200">
            <thead class="bg-gray-100">
                <tr>
                    <th class="px-4 py-2 border">ID</th>
                    <th class="px-4 py-2 border">Value</th>
                    <c:if test="${type eq 'category'}">
                        <th class="px-4 py-2 border">Father</th>
                    </c:if>
                    <c:if test="${type eq 'shippingMethod'}">
                        <th class="px-4 py-2 border">Price</th>
                    </c:if>
                    <th class="px-4 py-2 border">Status</th>
                    <th class="px-4 py-2 border">Action</th>
                </tr>
            </thead>
            <tbody>
                <form id="editForm" action="MainController" method="post" style="display: inline"></form>

                <c:forEach var="dto" items="${list}">
                    <c:set var="id" value="${dto.id}" />
                    <c:choose>
                        <c:when test="${dto.id == editId && type eq editType}">
                            <tr class="bg-yellow-50">
                                <td class="px-4 py-2 border">
                                    <input form="editForm" type="text" name="id" value="${dto.id}" readonly class="bg-gray-100 cursor-not-allowed px-2 py-1" />
                                </td>
                                <td class="px-4 py-2 border">
                                    <input form="editForm" type="text" name="value" value="${dto[fieldName]}" class="px-2 py-1 border rounded" />
                                </td>

                                <c:if test="${type eq 'category'}">
                                    <td class="px-4 py-2 border">
                                        <select form="editForm" name="parentId" class="px-2 py-1 border rounded">
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
                                    <td class="px-4 py-2 border">
                                        <input form="editForm" type="number" name="price" value="${dto.price}" class="px-2 py-1 border rounded" />
                                    </td>
                                </c:if>

                                <td class="px-4 py-2 border">
                                    <select form="editForm" name="isActive" class="px-2 py-1 border rounded">
                                        <option value="${true}" ${dto.is_active ? 'selected' : ''}>Active</option>
                                        <option value="${false}" ${!dto.is_active ? 'selected' : ''}>Not Active</option>
                                    </select>
                                </td>

                                <td class="px-4 py-2 border">
                                    <input form="editForm" type="hidden" name="type" value="${type}" />
                                    <input form="editForm" type="hidden" name="action" value="updateSystemConfig" />
                                    <button form="editForm" type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded shadow">Save</button>
                                    <form action="MainController" method="post" style="display: inline">
                                        <button name="action" value="toSystemConfigManagement" class="bg-gray-500 hover:bg-gray-600 text-white px-3 py-1 rounded shadow">Cancel</button>
                                    </form>
                                </td>
                            </tr>
                        </c:when>

                        <c:otherwise>
                            <tr class="hover:bg-gray-50">
                                <td class="px-4 py-2 border">${dto.id}</td>
                                <td class="px-4 py-2 border">${dto[fieldName]}</td>
                                <c:if test="${type eq 'shippingMethod'}">
                                    <td class="px-4 py-2 border">${dto.price}</td>
                                </c:if>
                                <c:if test="${type eq 'category'}">
                                    <td class="px-4 py-2 border">${dto.parent_category_id == -1? '-': dto.parent_category_id}</td>
                                </c:if>
                                <td class="px-4 py-2 border ${dto.is_active ? 'text-green-600' : 'text-red-600'}">
                                    ${dto.is_active ? 'Active': 'Not Active'}
                                </td>
                                <td class="px-4 py-2 border space-x-1">
                                    <form action="MainController" method="post" style="display:inline;">
                                        <input type="hidden" name="type" value="${type}" />
                                        <input type="hidden" name="fieldName" value="${fieldName}" />
                                        <input type="hidden" name="editType" value="${type}" />
                                        <input type="hidden" name="action" value="toSystemConfigManagement" />
                                        <button type="submit" name="editId" value="${dto.id}" class="bg-black hover:bg-gray-800 text-white px-3 py-1 rounded shadow">Edit</button>
                                    </form>

                                    <form action="MainController" method="post" style="display:inline;">
                                        <input type="hidden" name="type" value="${type}" />
                                        <input type="hidden" name="id" value="${dto.id}" />
                                        <input type="hidden" name="status" value="${dto.is_active}" />
                                        <input type="hidden" name="action" value="toggleIsActiveSystemConfig" />
                                        <button type="submit"
                                                style="${dto.is_active 
            ? 'background-color:#ff4d4f;color:white;' 
            : 'background-color:white;border:1px solid #ff4d4f;color:#ff4d4f;'}"
                                                onclick="return confirm('${dto.is_active? 'Deactivate': 'Activate'} this item?')">
                                            ${dto.is_active? 'Deactivate': 'Activate'}
                                        </button>
                                    </form> 
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${empty editId && (addModeType eq type)}">
                    <form id="addForm" action="MainController" method="post" style="display: inline"></form>
                    <tr class="bg-green-50">
                        <td class="px-4 py-2 border text-gray-500">(New)</td>
                        <td class="px-4 py-2 border">
                            <input form="addForm" type="text" name="value" placeholder="Enter value" required class="px-2 py-1 border rounded" />
                        </td>

                        <c:if test="${type eq 'category'}">
                            <td class="px-4 py-2 border">
                                <select form="addForm" name="parentId" class="px-2 py-1 border rounded">
                                    <option value="-1">-- Không có --</option>
                                    <c:forEach var="cat" items="${categoryList}">
                                        <option value="${cat.id}">${cat.name}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </c:if>
                        <c:if test="${type eq 'shippingMethod'}">
                            <td class="px-4 py-2 border">
                                <input form="addForm" type="number" name="price" placeholder="Enter price" required min="0" step="0.01" class="px-2 py-1 border rounded" />
                            </td>
                        </c:if>
                        <td class="px-4 py-2 border text-green-600">Active</td>
                        <td class="px-4 py-2 border space-x-1">
                            <input form="addForm" type="hidden" name="type" value="${type}" />
                            <input form="addForm" type="hidden" name="action" value="addSystemConfig" />
                            <button form="addForm" type="submit" class="bg-green-600 hover:bg-green-700 text-white px-3 py-1 rounded shadow">Lưu mới</button>
                            <form action="MainController" method="post" style="display: inline">
                                <button name="action" value="toSystemConfigManagement" class="bg-gray-500 hover:bg-gray-600 text-white px-3 py-1 rounded shadow">Hủy</button>
                            </form>
                        </td>
                    </tr>
                </c:if>

                <c:if test="${empty list}">
                    <tr>
                        <td colspan="6" class="px-4 py-2 text-center text-gray-500">No Data Found.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <c:if test="${empty editId && (addModeType eq '' || addModeType ne type)}">
            <form action="MainController" method="post" class="mt-4">
                <input type="hidden" name="action" value="toSystemConfigManagement" />
                <input type="hidden" name="addModeType" value="${type}" /> 
                <button type="submit" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded shadow">Thêm Mới</button>
            </form>
        </c:if>
    </div>
</body>

</html>
