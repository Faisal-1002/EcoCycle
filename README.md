### ♻️ EcoCycle (Capstone 03)

Developed as part of the **EcoCycle** recycling management system — a Capstone 03 project. I served as the **Team Leader** and was responsible for designing, implementing, and securing multiple custom backend endpoints focused on user flow, container logistics, and automation.

These endpoints go beyond standard CRUD, integrating smart business logic, automated scheduling, and external services (email & WhatsApp).

---

### 📊 Summary

- 🧠 **Role**: Team Leader & Backend Developer
- 🔧 **Total Custom Endpoints**: 16
- 🔐 **Secured**: Yes (JWT-based Role Access)
- 📦 **Handled Models**: User, Collector, ContainerRequest, Container
- 🔔 **Includes Integrations**: Email + WhatsApp

---

### 📌 Endpoint Table

| #   | Endpoint Description                        | Purpose / Logic                                           | Author  |
|------|---------------------------------------------|------------------------------------------------------------|---------|
| 1    | **Get user by ID**                          | Fetch detailed user info                                  | Faisal  |
| 2    | **Get all container requests by user ID**   | Track container history per user                          | Faisal  |
| 4    | **Get all available containers**            | Return container list not in use                          | Faisal  |
| 5    | **Get collector by ID**                     | Show collector profile and availability                   | Faisal  |
| 8    | **Request container**                       | Submit a new container delivery request                   | Faisal  |
| 9    | **Get container request by ID**             | Fetch full request details                                | Faisal  |
| 10   | **Request container replacement**           | Mark request for replacement with reason tracking         | Faisal  |
| 11   | **Process container replacement**           | Handles collector assignment and status update            | Faisal  |
| 12   | **Accept container request**                | Internal approval step before dispatch                    | Faisal  |
| 15   | **Get requests by collector ID**            | Returns requests assigned to a specific collector         | Faisal  |
| 16   | **Notify user by email (container)**        | Trigger email confirmation or update                      | Faisal  |
| 17   | **Send WhatsApp (container)**               | Integration with WhatsApp API to notify users             | Faisal  |
| 20   | **Auto pickup request after 7 days**        | Cron-based logic to auto-schedule pickup if no action     | Faisal  |
| 22   | **Mark request as picked up**               | Updates status and finalizes request                      | Faisal  |

---

### ⚙️ Highlights

- ✅ Clean **controller → service → repository** structure
- ✅ Integrated with **Email** + **WhatsApp APIs**
- ✅ Smart logic for request flow and auto-replacement
- ✅ Automated fallback: pickup scheduling after 7 days
- ✅ Built with **Spring Security** and strict role enforcement (Admin, Collector, User)

---

### 📦 Suggested Enhancements (Future Work)

- 📍 Add container location tracking (GPS-based)
- 📅 Add pickup scheduling calendar (user selects preferred time)
- ⏱ Auto-expire requests if not accepted in time
- 🧠 Predictive analytics for container usage trends

---

<h3 align="center">🔗 Connect with me</h3>

<p align="center">
  <a href="https://www.linkedin.com/in/-faisal-al-ghamdi/" target="_blank">
    <img src="https://img.shields.io/badge/LinkedIn-blue?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn Badge"/>
  </a>
</p>
