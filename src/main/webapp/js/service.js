
// Daemon div click listener to sub page
var showList = function(id) {
	var url="/monitor/daemonDetail?serverId="+id;
	$(location).attr('href',url);
}