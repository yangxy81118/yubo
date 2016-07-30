package com.yubo.feeder.vo;

public class SvgVO {

	private Integer svgId;

	private String svgTag;

	private String svgContent;

	/**
	 * svg文件大小（kb）
	 */
	private Double svgLength;

	/**
	 * 这个纯粹为了前端显示时定位用，js模块有bug
	 */
	private Integer svgIconIndex;

	public Integer getSvgIconIndex() {
		return svgIconIndex;
	}

	public void setSvgIconIndex(Integer svgIconIndex) {
		this.svgIconIndex = svgIconIndex;
	}

	public Double getSvgLength() {
		return svgLength;
	}

	public void setSvgLength(Double svgLength) {
		this.svgLength = svgLength;
	}

	public Integer getSvgId() {
		return svgId;
	}

	public void setSvgId(Integer svgId) {
		this.svgId = svgId;
	}

	public String getSvgTag() {
		return svgTag;
	}

	public void setSvgTag(String svgTag) {
		this.svgTag = svgTag;
	}

	public String getSvgContent() {
		return svgContent;
	}

	public void setSvgContent(String svgContent) {
		this.svgContent = svgContent;
	}

	@Override
	public String toString() {
		return "SvgVO [svgId=" + svgId + ", svgTag=" + svgTag + ", svgContent="
				+ svgContent + ", svgLength=" + svgLength + ", svgIconIndex="
				+ svgIconIndex + "]";
	}

}
