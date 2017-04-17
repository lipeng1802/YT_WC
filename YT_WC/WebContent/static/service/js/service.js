/**
 * 业务js 
 * 固定配置菜单路径
 */
var tabName={
		"花色配置":"/jeeplus/a/bb/colorConfig/colorList",
}

/**
 * 根据SRC定位iframe
 * @param src
 * @returns
 */
function getIframeBySrc(src){
	return $(top.$parentNode).find("iframe[src='"+src+"']").first();
}
/**
 * 指定执行哪个iframe下的刷新
 * @param src
 * @param button
 */
function directIframeRefresh(src,button){
	getIframeBySrc(src)[0].contentWindow.document.getElementById(button).click()
}