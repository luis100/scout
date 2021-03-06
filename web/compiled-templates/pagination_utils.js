function Paginator(resourceCount, resourceAPI, pageSize, templateName, divID,
		contextPath, mustacheletPath) {
	this.resourceCount = resourceCount;
	this.resourceAPI = resourceAPI;
	this.pageSize = pageSize;
	this.templateName = templateName;
	this.divID = divID;
	this.contextPath = contextPath;
	this.mustacheletPath = mustacheletPath;

	if (window === this) {
		return new Paginator(resourceCount, resourceAPI, pageSize,
				templateName, divID, contextPath, mustacheletPath);
	}

	return this;
}

Paginator.prototype = {
	updateList : function(start) {
		document.location = $.param.fragment(window.location.href, 'start='
				+ start);

	},

	init : function() {
		var paginator = this;
		$(window).bind('hashchange', function(e) {
			var params = $.deparam.fragment(true);
			if (params && params.start && params.start > 0) {
				paginator.updateListImpl(params.start);
			} else {
				paginator.updateListImpl(0);
			}
		});
		$(document).ready(function() {
			$(window).trigger('hashchange');
		});
	},
	updateListImpl : function(start) {
		if (start >= resourceCount) {
			start = resourceCount - pageSize;
		}
		if (start < 0) {
			start = 0;
		}

		var end = start + pageSize < resourceCount ? start + pageSize
				: resourceCount;
		var next = end < resourceCount ? end : start;
		var previous = start - pageSize < 0 ? 0 : start - pageSize;

		var apiRequest = jQuery.param.querystring(contextPath + resourceAPI,
				'start=' + start + '&max=' + pageSize);
		
		// XXX fix array notation
		apiRequest = apiRequest.replace(/%5B%5D=/g, "=");

		$.getJSON(apiRequest, function(data) {
			var template = Handlebars.templates[templateName];
			var context = {
				items : data,
				index_start : start + 1,
				index_end : end,
				count : resourceCount,
				index_previous : previous,
				index_next : next,
				index_previous_disabled : start == 0,
				index_next_disabled : end == resourceCount,
				contextPath : contextPath,
				mustacheletPath : mustacheletPath
			};
			var baseTemplate = Handlebars.templates['base_paginated_list.hbs'];
			Handlebars.registerPartial("base_paginated_list", baseTemplate);
			var html = template(context);
			$(divID).html(html);
		});
	}
};
