<%-- 
    Document   : admin-item-management
    Created on : 15-Jul-2025, 15:16:17
    Author     : ho huy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Item Management</title>
        <link rel="stylesheet" href="assets/css/address-management.css">
        </head>
    <body>
        <div class="main-container">
            <h1>Product Item Management</h1>
            <hr>

            <h2>Variations & Options</h2>
            <div class="top-actions">
                <form action="MainController" method="post" style="display: inline;">
                    <input type="hidden" name="productId" value="${param.productId}"/>
                    <input type="text" name="variationName" value="${param.variationName}" required placeholder="Variation Name"/>
                    <button name="action" value="addVariation">Add Variation</button>
                </form>
            </div>

            <c:forEach var="variation" items="${variationList}">
                <div class="variation-section">
                    <c:choose>
                        <c:when test="${param.editVariationId == variation.id}">
                            <div class="variation-edit-mode">
                                <h3></h3> <form action="MainController" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="updateVariation"/>
                                    <input type="hidden" name="variationId" value="${variation.id}"/>
                                    <input type="hidden" name="productId" value="${param.productId}"/>
                                    <input type="text" name="variationName" value="${variation.name}" required/>
                                    <button type="submit"
                                            onclick="return confirm('Updating this variation may affect product items. Continue?')">
                                        Update
                                    </button>
                                    <button form="cancel-3" type="submit">Cancel</button>
                                </form>
                                <form id="cancel-3" action="MainController" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="toAdminProductItemPage"/>
                                    <input type="hidden" name="productId" value="${param.productId}"/>
                                </form>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="variation-display-mode">
                                <h3>${variation.name}</h3>
                                
                                <form action="MainController" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="toAdminProductItemPage"/>
                                    <input type="hidden" name="editVariationId" value="${variation.id}"/>
                                    <input type="hidden" name="productId" value="${param.productId}"/>
                                    <button type="submit">Edit</button>
                                </form>

                                <form action="MainController" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="removeVariation"/>
                                    <input type="hidden" name="variationId" value="${variation.id}"/>
                                    <input type="hidden" name="productId" value="${param.productId}"/>
                                    <button type="submit" class="button-danger"
                                            onclick="return confirm('This will remove the variation and all related options/items. Proceed?')">
                                        Remove
                                    </button>
                                </form>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    
                    <form action="MainController" method="post" style="display: inline;">
                        <input type="hidden" name="variationId" value="${variation.id}"/>
                        <input type="hidden" name="productId" value="${param.productId}"/>
                        <input type="text" name="optionValue" value="" required placeholder="Option Value"/>
                        <button name="action" value="addOption">Add Option</button>
                    </form>
                    <ul class="option-list">
                        <c:set var="count" value="0" />
                        <c:forEach var="option" items="${optionList}">
                            <c:if test="${option.variation_id == variation.id}">
                                <li class="option-item">
                                    <c:choose>
                                        <c:when test="${param.editOptionId == option.id}">
                                            <div class="option-edit-mode">
                                                <form method="post" action="MainController" style="display:inline;">
                                                    <input type="hidden" name="action" value="updateOption" />
                                                    <input type="hidden" name="optionId" value="${option.id}" />
                                                    <input type="hidden" name="variationId" value="${variation.id}" />
                                                    <input type="hidden" name="productId" value="${param.productId}" />

                                                    <input type="text" name="optionValue" value="${option.value}" required />
                                                    <button type="submit" 
                                                            onclick="return confirm('Some option may get deleted by this action! Are you sure to update this option?')"
                                                            >Update</button>
                                                    <button form="cancel-2" type="submit">Cancel</button>
                                                </form>
                                                <form id="cancel-2" method="post" action="MainController" style="display:inline;">
                                                    <input type="hidden" name="action" value="toAdminProductItemPage" />
                                                    <input type="hidden" name="productId" value="${param.productId}" />
                                                </form>
                                            </div>
                                        </c:when>

                                        <c:otherwise>
                                            <div class="option-display-mode">
                                                Option: ${option.value}
                                                <form method="post" action="MainController" style="display:inline;">
                                                    <input type="hidden" name="action" value="toAdminProductItemPage" />
                                                    <input type="hidden" name="editOptionId" value="${option.id}" />
                                                    <input type="hidden" name="productId" value="${param.productId}" />
                                                    <button type="submit">Edit</button>
                                                </form>
                                                <form method="post" action="MainController" style="display:inline;">
                                                    <input type="hidden" name="action" value="removeOption" />
                                                    <input type="hidden" name="optionId" value="${option.id}" />
                                                    <input type="hidden" name="variationId" value="${variation.id}" />
                                                    <input type="hidden" name="productId" value="${param.productId}" />
                                                    <button type="submit" class="button-danger" onclick="return confirm('Are you sure to delete this option?')">Remove</button>
                                                </form>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

                                    <c:set var="count" value="${count + 1}" />
                                </li>
                            </c:if>
                        </c:forEach>

                        <c:if test="${count == 0}">
                            <li class="no-option-msg">No Option in this variation</li>
                        </c:if>
                    </ul>
                </div>
            </c:forEach>

            <h2>Product Item List (SKU)</h2>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>SKU</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Image</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty itemList}">
                        <tr>
                            <td colspan="6" class="no-items-found">No product items available.</td>
                        </tr>
                    </c:if>
                    
                    <c:forEach var="item" items="${itemList}">
                        <c:set var="id" value="${item.id}" />
                        <c:choose>
                            <c:when test="${id == editId}">
                                <tr>
                                    <td><input form="edit-form" type="text" name="sku" value="${item.sku}" readonly class="input-readonly" /></td>
                                    <td><input form="edit-form" type="text" name="price" value="${item.price}" inputmode="numeric" pattern="^\d+(\.\d{1,2})?$" class="input-field" /></td>
                                    <td><input form="edit-form" type="text" name="stock" value="${item.quantity_in_stock}" inputmode="numeric" pattern="[0-9]*" class="input-field" /></td>
                                    <td><img src="${item.item_image_link}" alt="${item.sku}" width="50" class="item-image"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.is_active}">✅</c:when>
                                            <c:otherwise>❌</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <input form="edit-form" type="hidden" name="productId" value="${param.productId}" />
                                        <input form="cancel" type="hidden" name="productId" value="${param.productId}" />
                                        <input form="edit-form" type="hidden" name="itemId" value="${item.id}" />
                                        <button form="edit-form" type="submit" name="action" value="updateProductItem">Save</button>
                                        <button form="cancel" name="action" value="toAdminProductItemPage" class="button-secondary">Cancel</button>
                                        <form id="edit-form" action="MainController" method="post"></form>
                                    </td>
                                </tr>

                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td>${item.sku}</td>
                                    <td>${item.price}</td>
                                    <td>${item.quantity_in_stock}</td>
                                    <td><img src="${item.item_image_link}" alt="${item.sku}" width="50" class="item-image"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.is_active}">✅</c:when>
                                            <c:otherwise>❌</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <form action="MainController" method="post" style="display:inline;">
                                            <input type="hidden" name="editId" value="${item.id}" />
                                            <input type="hidden" name="productId" value="${param.productId}" />
                                            <button type="submit" name="action" value="toAdminProductItemPage">Edit</button>
                                        </form>
                                        <form action="MainController" method="post" style="display:inline;">
                                            <input type="hidden" name="itemId" value="${item.id}" />
                                            <input type="hidden" name="productId" value="${param.productId}" />
                                            <button type="submit" name="action" value="toggleIsActiveProductItem"
                                                    class="${item.is_active ? 'button-secondary' : 'button-success'}">
                                                ${item.is_active ? 'Deactivate' : 'Activate'}
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </tbody>
            </table>
            <form id="cancel" action="MainController" method="post" style="display:inline;">
                
            </form>
        </div>
        <hr>
        <div class="back-button-container">
            <form action="MainController" method="post" style="display:inline;">
                <button name="action" value="toAdminProductPage">Back</button>
            </form>
        </div>
    </body>
</html>