FROM openjdk:11
EXPOSE 8080
ADD ./target/meals-app-0.0.1-SNAPSHOT.jar top-restaurant-api.jar
ENTRYPOINT ["java","-jar","top-restaurant-api.jar"]

CMD java -cp target com.richards.mealsapp.MealsAppApplication