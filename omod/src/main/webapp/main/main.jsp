<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="../includes/js_css.jsp" %>
<br/>
<openmrs:require privilege="Access Laboratory" otherwise="/login.htm" redirect="/module/laboratory/main.form" />
<%@ include file="../localHeader.jsp" %>
<%@ include file="/WEB-INF/template/footer.jsp" %>  