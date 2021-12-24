# target area: x=277..318, y=-92..-53
target_x = (277, 318)
target_y = (-92, -53)

def in_target(x, y):
    return target_x[0] <= x <= target_x[1] and target_y[0] <= y <= target_y[1]


def overshot(x, y):
    return x > target_x[1] or y < target_y[0]


def step(x, y, vx, vy):
    x = x + vx
    y = y + vy
    if vx > 0:
        vx = vx - 1
    if vx < 0:
        vx = vx + 1
    vy = vy - 1
    return x, y, vx, vy


def shoot(init_vx, init_vy):
    trajectory = []
    x = 0
    y = 0
    vx = init_vx
    vy = init_vy
    while not in_target(x, y) and not overshot(x, y):
        trajectory.append((x, y))
        x, y, vx, vy = step(x, y, vx, vy)
    if in_target(x, y):
        return trajectory
    else:
        return []


max_y = 0
cnt = 0
for vx in range(0, 500):
    for vy in range(-500, 500):
        traj = shoot(vx, vy)
        if len(traj) > 0:
            cnt += 1
            my = max(traj, key=lambda el: el[1])
            if my[1] > max_y:
                max_y = my[1]

print(max_y)
print(cnt)