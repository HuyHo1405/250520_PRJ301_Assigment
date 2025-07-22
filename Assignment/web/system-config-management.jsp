<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>System Configuration Management</title>
    <link rel="stylesheet" href="assets/css/system-config-management.css"/>
</head>
<body>
  <div class="layout-container">
      <jsp:include page="assets/components/sidebar.jsp" />

    <div class="content">

        <h1>System Configuration Management - Version ${appVersion}</h1>
            <div class="config-section" id="orderStatusHeader">
                <div class="section-title">Order Status</div>
                <jsp:include page="config-table.jsp">
                    <jsp:param name="listName" value="orderStatusList" />
                    <jsp:param name="type" value="orderStatus" />
                    <jsp:param name="editType" value="${editType}" />
                    <jsp:param name="fieldName" value="status" />
                </jsp:include>
            </div>

            <div class="config-section" id="paymentTypeHeader">
                <div class="section-title">Payment Types</div>
                <jsp:include page="config-table.jsp">
                    <jsp:param name="listName" value="paymentTypeList" />
                    <jsp:param name="type" value="paymentType" />
                    <jsp:param name="editType" value="${editType}" />
                    <jsp:param name="fieldName" value="value" />
                </jsp:include>
            </div>

            <div class="config-section" id="shippingMethodHeader">
                <div class="section-title">Shipping Methods</div>
                <jsp:include page="config-table.jsp">
                    <jsp:param name="listName" value="shippingMethodList" />
                    <jsp:param name="type" value="shippingMethod" />
                    <jsp:param name="editType" value="${editType}" />
                    <jsp:param name="fieldName" value="name" />
                </jsp:include>
            </div>

            <div class="config-section" id="countryHeader">
                <div class="section-title">Countries</div>
                <jsp:include page="config-table.jsp">
                    <jsp:param name="listName" value="countryList" />
                    <jsp:param name="type" value="country" />
                    <jsp:param name="editType" value="${editType}" />
                    <jsp:param name="fieldName" value="country_name" />
                </jsp:include>
            </div>

            <div class="config-section" id="categoryHeader">
                <div class="section-title">Categories</div>
                <jsp:include page="config-table.jsp">
                    <jsp:param name="listName" value="categoryList" />
                    <jsp:param name="type" value="category" />
                    <jsp:param name="editType" value="${editType}" />
                    <jsp:param name="fieldName" value="name" />
                </jsp:include>
            </div>

            <div class="actions">
                <form action="MainController" method="POST" style="display: inline">
                    <button name="action" value="toWelcome">Back to Home</button>
                </form>
            </div>
        </div>

    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const editType = "${editType}";
            const addModeType = "${addModeType}";
            let targetType = editType || addModeType;

            if (targetType) {
                window.location.hash = targetType + 'Header';
            }

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
                    form.addEventListener('submit', function () {
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
