# Support Ticket Example in DDD

### Task description
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



### Entities
- Member
  - id
  - name
  - role
    - 客戶 Customer
    - 客服 CustomerServiceOperator
    - 客服經理 CustomerServiceManager

- SupportTicket
  - id
  - level
    - LOW 
    - MEDIUM 
    - HIGH 
    - EMERGENCY
  - timeLimit
  - title
  - status
    - OPEN
    - REPORTED
    - CLOSED
  - customerId
  - assignedOperatorId
  - supportTicketRecordList
    - id
    - content
    - posterId


### UseCase

客戶 UseCase
- [x] 開啟工單
- [x] 回覆工單
- [ ] 上報公單
- [ ] 關閉
- [ ] 關閉上報公單
- [ ] 重新開啟工單


客服 UseCase
- [ ] 查看工單
- [ ] 回覆工單
- [ ] 關閉公單


經理 UseCase
- [ ] 查看工單
- [ ] 回覆工單
- [ ] 關閉公單
- [ ] 關閉上報公單
- [ ] 分配上報工單

#### SupportTicket UseCase

- [x] 開啟工單
- 客戶需要在工單上描述問題，填寫title與內容，並給選擇優先級
- 由優先級決定處理時限
- 隨機選擇客服人員分配工單
- 建立工單 SupportTicket
- 建立工單紀錄 SupportTicketRecord ，並將客戶問題的內容紀錄在上面


- [x] 回覆工單
- 客戶/客服/經理 都可以回覆
  - 客戶需要檢查是否為自己的工單
  - 客服只能回覆被指派的工單
- 須檢查工單狀態 = OPEN | REPORTED
- 建立工單紀錄 SupportTicketRecord ，並將回覆內容與回覆者紀錄在上面


- [x] 查看工單
- temporally permit for all

