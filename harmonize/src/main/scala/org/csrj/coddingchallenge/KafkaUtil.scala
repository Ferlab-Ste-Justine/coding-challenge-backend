package org.csrj.coddingchallenge

import java.util.Properties

import org.slf4j.LoggerFactory


import org.apache.kafka.clients.admin.AdminClient

object KafkaUtil {
  private val log = LoggerFactory.getLogger("KafkaUtil")

  def waitForTopicExists(topic: String, config: Properties, retries: Int = 1000 ): Boolean = {
    val admin = AdminClient.create(config)
    var exist = admin.listTopics().names().get().contains(topic)
    var remainingRetry = retries
    while (!exist && remainingRetry > 0) {
      log.info(s"Topic $topic does not exist, wait for its creation. $remainingRetry retries left")
      exist = admin.listTopics().names().get().contains(topic)
      remainingRetry -= 1
      Thread.sleep(1000)
    }
    exist
  }
}
