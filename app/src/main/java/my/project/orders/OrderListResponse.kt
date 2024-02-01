package my.project.orders

import my.project.api.base.Response

data class OrderListItem(
    val slug: String,
    val orderedOnDateTime: String,
    val deliveredOnDateTime: String,
    val totalPriceWithTax: Double,
    val orderStatus: String
)

data class OrderListResponse(
    val messages: List<OrderListItem>
) : Response()
