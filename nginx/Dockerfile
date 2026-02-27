FROM nginx:stable-alpine

# Remove default config
RUN rm /etc/nginx/conf.d/default.conf

# Copy custom config
COPY default.conf /etc/nginx/conf.d/

# Copy frontend build
COPY ../frontend/build /usr/share/nginx/html

# Expose ports
EXPOSE 80 443

CMD ["nginx", "-g", "daemon off;"]