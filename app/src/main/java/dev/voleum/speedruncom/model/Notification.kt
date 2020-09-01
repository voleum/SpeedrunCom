package dev.voleum.speedruncom.model

data class Notification(val id: String,
                        val created: String,
                        val status: String,
                        val text: String,
                        val item: Link,
                        val links: List<Link>)