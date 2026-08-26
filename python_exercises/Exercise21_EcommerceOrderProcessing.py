from pyspark.sql import SparkSession

spark = SparkSession.builder \
    .appName("EcommerceOrderProcessing") \
    .master("local[*]") \
    .getOrCreate()

sc = spark.sparkContext

# --------------------------------
# 1. Input data
# --------------------------------

orders = [
    ("1", "C101", "P101", 2, 1000),
    ("2", "C102", "P102", 1, 500),
    ("3", "C101", "P103", 3, 1500),
    ("4", "C103", "P101", 1, 500),
    ("5", "C102", "P104", 2, 2000),
    ("6", "C104", "P999", 1, -500)
]

orders_rdd = sc.parallelize(orders)


# --------------------------------
# 2. Broadcast variable
# --------------------------------

discounts = {
    "P101": 0.10,
    "P102": 0.05,
    "P103": 0.20,
    "P104": 0.15
}

discount_broadcast = sc.broadcast(discounts)


# --------------------------------
# 3. Accumulator
# --------------------------------

invalid_orders = sc.accumulator(0)


# --------------------------------
# 4. Validate orders
# --------------------------------

def validate_order(order):

    product_id = order[2]
    amount = order[4]

    if amount <= 0:
        invalid_orders.add(1)
        return False

    if product_id not in discount_broadcast.value:
        invalid_orders.add(1)
        return False

    return True


valid_orders = orders_rdd.filter(validate_order)


# --------------------------------
# 5. Apply discount
# --------------------------------

discounted_orders = valid_orders.map(
    lambda order: (
        order[0],
        order[2],
        order[4] * (
            1 - discount_broadcast.value[order[2]]
        )
    )
)


# --------------------------------
# 6. Trigger execution
# --------------------------------

results = discounted_orders.collect()


print("Discounted Orders")

for result in results:
    print(result)


print("Invalid Orders:", invalid_orders.value)


spark.stop()
