# FROM lolhens/baseimage-openjre
FROM openjdk:17-oracle
# ADD target/back-end-mock-1.jar back-end-mock-1.jar

COPY target/back-end-mock-1.jar back-end-mock-1.jar
EXPOSE 80

ENTRYPOINT ["java", "-jar", "back-end-mock-1.jar"]

# Add Jenkins user and grant sudo privileges
RUN adduser -u 1000 jenkins && \
    echo "jenkins ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

# Create Jenkins user's home directory and .bashrc file
RUN mkdir -p /home/jenkins && \
    touch /home/jenkins/.bashrc && \
    chown -R jenkins:jenkins /home/jenkins

# Add alias to .bashrc for Jenkins user
RUN echo "alias docker='sudo docker '" >> /home/jenkins/.bashrc
