const API_URL = process.env.EXPO_PUBLIC_API_URL;

export async function postSyncBatch(token: string, items: unknown[]) {
  const response = await fetch(`${API_URL}/sync/catches`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` },
    body: JSON.stringify({ items })
  });
  if (!response.ok) throw new Error('Sync batch failed');
  return response.json();
}
