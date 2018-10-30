package com.newqur.spider.vo;

import com.newqur.spider.cache.Resource;

/**
 * 对应 {@link Resource}
 * @author freesaas
 */
public class ResourceVO extends BaseVO{
	private Resource resource;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
}
