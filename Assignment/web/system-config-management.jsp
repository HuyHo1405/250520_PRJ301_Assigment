<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="assets/css/system-config.css"/>
        <title>System Configuration Management</title>
    </head>
    <body>
            <h1>System Configuration Management - Version ${appVersion}</h1>

            <hr>
            <div class="config-section">
            <h2 id="orderStatusHeader">Order Status</h2>
            <jsp:include page="config-table.jsp">
                <jsp:param name="listName" value="orderStatusList" />
                <jsp:param name="type" value="orderStatus" />
                <jsp:param name="editType" value="${editType}" />
                <jsp:param name="fieldName" value="status" />
            </jsp:include>
            </div>
            <div class="config-section">
            <h2 id="paymentTypeHeader">Payment Types</h2>
            <jsp:include page="config-table.jsp">
                <jsp:param name="listName" value="paymentTypeList" />
                <jsp:param name="type" value="paymentType" />
                <jsp:param name="editType" value="${editType}" />
                <jsp:param name="fieldName" value="value" />
            </jsp:include>
            </div>
            <div class="config-section">
            <h2 id="shippingMethodHeader">Shipping Methods</h2>
            <jsp:include page="config-table.jsp">
                <jsp:param name="listName" value="shippingMethodList" />
                <jsp:param name="type" value="shippingMethod" />
                <jsp:param name="editType" value="${editType}" />
                <jsp:param name="fieldName" value="name" />
            </jsp:include>
            </div>
            <div class="config-section">
            <h2 id="countryHeader">Countries</h2>
            <jsp:include page="config-table.jsp">
                <jsp:param name="listName" value="countryList" />
                <jsp:param name="type" value="country" />
                <jsp:param name="editType" value="${editType}" />
                <jsp:param name="fieldName" value="country_name" />
            </jsp:include>
           </div>
           
            <div class="config-section">
            <h2 id="categoryHeader">Categories</h2>
            <jsp:include page="config-table.jsp">
                <jsp:param name="listName" value="categoryList" />
                <jsp:param name="type" value="category" />
                <jsp:param name="editType" value="${editType}" />
                <jsp:param name="fieldName" value="name" />
            </jsp:include>
            </div>
            <hr>

            <form action="MainController" method="POST" style="display: inline">
                <button name="action" value="toWelcome">Back to Home</button>
            </form>

            <script>
                document.addEventListener('DOMContentLoaded', function() {
                // Get the 'editType' and 'addModeType' parameters passed from the server.
                const editType = "${editType}";
                const addModeType = "${addModeType}";

                // Determine the target section based on which parameter is set.
                // 'editType' takes precedence, then 'addModeType'.
                let targetType = editType;
                if (!targetType) { // If editType is empty, check addModeType
                    targetType = addModeType;
                }

                // If a target type is present, set the window hash to jump to the corresponding header.
                // This will make the page load directly at that section.
                if (targetType) {
                    window.location.hash = targetType + 'Header';
                }

                // --- Original JavaScript logic (kept for completeness) ---
                function addAnchorToFormAction(form, currentTableType) {
                    if (!form.action.includes('#')) {
                        form.action += '#' + currentTableType + 'Header';
                    }
                }

                const allForms = document.querySelectorAll('form');
                allForms.forEach(form => {
                    const hiddenTypeInput = form.querySelector('input[name="type"]');
                    if (hiddenTypeInput) {
                        const currentTableType = hiddenTypeInput.value;
                        form.addEventListener('submit', function() {
                            addAnchorToFormAction(this, currentTableType);
                        });
                    }
                });

                const cancelLinks = document.querySelectorAll('a[href*="action=toSystemConfigManagement"]');
                cancelLinks.forEach(link => {
                    const url = new URL(link.href);
                    const type = url.searchParams.get('type');
                    if (type && !url.hash) {
                        link.href = url.origin + url.pathname + url.search + '#' + type + 'Header';
                    }
                });
                });
            </script>
    </body>
</html>
