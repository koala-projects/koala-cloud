package cn.koala.cloud.gateway.task;

import lombok.NonNull;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 刷新路由任务
 *
 * @author Houtaroy
 */
public class RefreshRoutesTask implements ApplicationEventPublisherAware {

  private ApplicationEventPublisher publisher;

  @Override
  public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
    this.publisher = applicationEventPublisher;
  }

  @Scheduled(fixedDelay = 60 * 1000)
  public void refreshRoutes() {
    this.publisher.publishEvent(new RefreshRoutesEvent(this));
  }
}
