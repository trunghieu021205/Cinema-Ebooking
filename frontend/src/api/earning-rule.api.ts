import apiClient from '@/api/axios'
import type {
  EarningRuleResponse,
  CreateEarningRuleRequest,
  UpdateEarningRuleRequest,
} from '@/types/earning-rule'

export const earningRuleApi = {
  // Backend trả về List, không phân trang
  getAll: () =>
    apiClient.get<EarningRuleResponse[]>('/loyalty/earning-rules'),

  getById: (id: number) =>
    apiClient.get<EarningRuleResponse>(`/loyalty/earning-rules/${id}`),

  create: (body: CreateEarningRuleRequest) =>
    apiClient.post<EarningRuleResponse>('/loyalty/earning-rules', body),

  update: (id: number, body: UpdateEarningRuleRequest) =>
    apiClient.put<EarningRuleResponse>(`/loyalty/earning-rules/${id}`, body),

  delete: (id: number) =>
    apiClient.delete(`/loyalty/earning-rules/${id}`),
}