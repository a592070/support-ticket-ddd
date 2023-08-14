# Support Ticket Example in DDD

task
- 客戶開啟客服工單，在工單上描述問題
- 工單會被隨機指派給客服人員
- 客戶與客服人員都需要透過工單聯絡，且所有聯繫都被追蹤
- 工單有優先級: 低、中、高、緊急，優先級會決定規定時限(Set Time Limit, SLA)
- 客服人員須在規定時限內回覆工單
- 如果未在 SLA 時間內回覆，客戶可以將工單上報給經理
- 上報會使得客服人員的回應時限降低 33% (deadline=上報時間點+SLA*0.66)
- 如果客服人員未在回應時限的 50% 內 (上報時間點+SLA*0.5) 開啟上報的工單，則會將它重新分配給其他客服人員
- 如果客戶未在 7 天內回覆客服人員問題，工單會被關閉，客戶可以重新開啟，但只能重新開啟過去 7 天內關閉的工單
- 上報的工單不能自動關閉，也不能被客服人員關閉，只能由客戶或客服經理關閉

flow
```sequence
Alice->Bob: Hello Bob, how are you?
Note right of Bob: Bob thinks
Bob-->Alice: I am good thanks!
```