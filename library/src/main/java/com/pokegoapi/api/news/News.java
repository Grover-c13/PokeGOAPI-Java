/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.api.news;

import POGOProtos.Data.News.CurrentNewsOuterClass;
import POGOProtos.Data.News.NewsArticleOuterClass.NewsArticle;
import POGOProtos.Networking.Requests.Messages.MarkReadNewsArticleMessageOuterClass.MarkReadNewsArticleMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.MarkReadNewsArticleResponseOuterClass.MarkReadNewsArticleResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.main.ServerRequestEnvelope;
import com.pokegoapi.util.Log;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class News {
	private static final java.lang.String TAG = News.class.getSimpleName();

	private final PokemonGo api;
	@Getter
	private CurrentNewsOuterClass.CurrentNews currentNews;
	// the Key of the Language = Key

	/**
	 * Constructor
	 *
	 * @param api Pokemon Go Core Api
	 */
	public News(PokemonGo api) {
		this.api = api;
	}

	public void setCurrentNews(CurrentNewsOuterClass.CurrentNews currentNews) {
		this.currentNews = currentNews;
	}

	/**
	 * Mark Unread News to read
	 */
	public void markUnreadNews() {

		if (currentNews == null || currentNews.getNewsArticlesCount() <= 0) {
			// do nothing
			return;
		}

		// Stored enabled and un-read article
		List<String> unReadNewsList = new ArrayList<>();
		for (NewsArticle newsArticle : currentNews.getNewsArticlesList()) {
			if (newsArticle.getEnabled() && !newsArticle.getArticleRead())
				unReadNewsList.add(newsArticle.getId());
		}

		Log.i(TAG, "markUnreadNews total Article count:" + unReadNewsList.size());

		if (unReadNewsList.size() > 0) {
			MarkReadNewsArticleMessage msg = MarkReadNewsArticleMessage.newBuilder()
					.addAllNewsIds(unReadNewsList).build();
			ServerRequest request = new ServerRequest(RequestTypeOuterClass.RequestType.MARK_READ_NEWS_ARTICLE, msg);
			ServerRequestEnvelope envelope = ServerRequestEnvelope.create(request);
			try {
				api.requestHandler.sendServerRequests(envelope);
				MarkReadNewsArticleResponse response = MarkReadNewsArticleResponse.parseFrom(request.getData());
				if (response.getResult() == MarkReadNewsArticleResponse.Result.SUCCESS) {
					Log.i(TAG, "Mark News Article -> success");
				} else {
					Log.w(TAG, "Mark News Article -> !success");
				}
			} catch (RequestFailedException e) {
				e.printStackTrace();
				Log.e(TAG, "RequestFailedException: cause:" + e.getCause() + " message:" + e.getMessage());
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
				Log.e(TAG, "InvalidProtocolBufferException: cause:" + e.getCause() + " message:" + e.getMessage());
			}
		} else {
			Log.i(TAG, "no unmarked news found -> skipped");
		}
	}
}
