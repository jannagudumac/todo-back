import subprocess
import os
import time

BACK_DIR  = os.path.dirname(os.path.abspath(__file__))
FRONT_DIR = os.path.join(BACK_DIR, '..', 'todo-front')
JAVA_HOME = '/usr/lib/jvm/java-1.17.0-openjdk-amd64'

# Kill anything already on 8080 / 4200
for port in [8080, 4200]:
    subprocess.run(f"fuser -k {port}/tcp 2>/dev/null", shell=True)

time.sleep(1)

back_cmd = (
    f'cd {BACK_DIR} && '
    f'JAVA_HOME={JAVA_HOME} ./mvnw spring-boot:run'
)

front_cmd = (
    f'cd {FRONT_DIR} && '
    f'npm start'
)

subprocess.Popen([
    'gnome-terminal', '--title=PlanIt — Backend',
    '--', 'bash', '-c', back_cmd + '; exec bash'
])

subprocess.Popen([
    'gnome-terminal', '--title=PlanIt — Frontend',
    '--', 'bash', '-c', front_cmd + '; exec bash'
])

print('Opened two terminal windows:')
print('  Backend  → http://localhost:8080')
print('  Frontend → http://localhost:4200  (ready in ~15s)')
