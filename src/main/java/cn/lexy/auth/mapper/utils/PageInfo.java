package cn.lexy.auth.mapper.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PageInfo {
	
	public PageInfo(){
		
	}
	
	private Long total;
	private Integer maxPage;
	private Integer current;
	private Integer pageSize;
	@JsonIgnore
	private Integer start;
	@JsonIgnore
	private Integer end;
	public Long getTotal() {
		return total==null?0:total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Integer getMaxPage() {
		return maxPage==null?0:maxPage;
	}
	public void setMaxPage(Integer maxPage) {
		this.maxPage = maxPage;
	}
	public Integer getCurrent() {
		return current;
	}
	public void setCurrent(Integer current) {
		this.current = current;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getStart(){
		if(null == start){
			getIndexs();
		}
		return this.start;
	}
	public Integer getEnd(){
		if(null == end){
			getIndexs();
		}
		return this.end;
	}
	private void getIndexs(){
		if(null == current){
			return ;
		}
		if(maxPage<=5){
			this.start = 1;
			this.end = maxPage;
			return ;
		}
		int pmax = current+5;
		if(pmax > maxPage){
			pmax = maxPage;
		}
		this.start = pmax-5;
		this.end = pmax;
	}


	/// 属性注入
	public void setPagesize(int size){
		this.pageSize = size;
	}

	public void setPageindex(int index){
		this.current = index;
	}
}