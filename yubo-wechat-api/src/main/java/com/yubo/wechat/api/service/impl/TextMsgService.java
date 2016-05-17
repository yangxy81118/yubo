package com.yubo.wechat.api.service.impl;

import com.yubo.wechat.api.service.MessageHandler;
import com.yubo.wechat.api.service.vo.MsgHandlerResult;

/**
 * 文本消息处理
 * @author young.jason
 *
 */
public class TextMsgService implements MessageHandler {

	public MsgHandlerResult execute(String requestBody) {

		// 2-文本回复
				// 2.1 - 激活码回复
				// 进入激活码验证流程
				// 验证成功，进行记录
				// 验证失败，反馈提示

				// 2.2 - 普通回复
				// 首先判断去Redis中进行查找，key为simpleTalk.${petId}.${userId}
				// 如果存在，则说明是普通立即回复，将该回复内容与redis对应value值存入到数据库，然后清除redis中该key
				// 如果不存在，则检查是否是命令回复，去检查数据库talk_history表
				// 若在talk_history中找到了，则说明是命令回复，将本次回复进行记录，进行对应的业务处理（学笑话，学外语等等）
				//---------上面这两种情况宠物是否需要再进行回复？暂无内容设计，定为不回复
				
				
				// 如果也没有找到，那么说明最一般的用户主动发文本消息过来，根据时间阶段进行摸Mo回复
		
		return null;
	}

}
