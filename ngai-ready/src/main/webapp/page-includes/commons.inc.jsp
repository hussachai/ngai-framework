<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="str" uri="http://commons.apache.org/lang/StringUtils" %>
<%@ taglib prefix="esc" uri="http://commons.apache.org/lang/StringEscapeUtils" %>
<%@ taglib prefix="locale" uri="http://commons.apache.org/lang/LocaleUtils" %>

<%@ taglib prefix="auth" uri="http://ngai.siberhus.com/ngai-guardian/tags" %>
<%@ taglib prefix="ui" uri="http://ngai.siberhus.com/ngai-ui/tags" %>
<%@ taglib prefix="l10n" uri="http://ngai.siberhus.com/ngai-localization/tags" %>
<%-- 
<%@ taglib prefix="auth" uri="/META-INF/tags/ngai-guardian.tld" %>
<%@ taglib prefix="ui" uri="/META-INF/tags/ngai-ui.tld" %>
<%@ taglib prefix="l10n" uri="/META-INF/tags/ngai-localization.tld" %>
--%>
<%@ taglib prefix="df" uri="http://ngai.siberhus.com/ngai-core/funcs" %>

<%-- Short hand for the context root. --%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<c:set var="layout" value="standard"></c:set>
<c:if test="${!empty param.layout}">
	<c:set var="layout" value="${param.layout}"></c:set>
</c:if>
